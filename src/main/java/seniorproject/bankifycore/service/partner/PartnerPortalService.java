package seniorproject.bankifycore.service.partner;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import seniorproject.bankifycore.domain.ClientApp;
import seniorproject.bankifycore.domain.ClientKeyRotationRequest;
import seniorproject.bankifycore.domain.ClientUser;
import seniorproject.bankifycore.domain.enums.ClientStatus;
import seniorproject.bankifycore.domain.enums.RotationStatus;
import seniorproject.bankifycore.dto.partner.PartnerPortalMeResponse;
import seniorproject.bankifycore.dto.rotation.RotateKeyRequest;
import seniorproject.bankifycore.dto.rotation.RotateKeyResponse;
import seniorproject.bankifycore.dto.rotation.RotationRequestItem;
import seniorproject.bankifycore.repository.ClientUserRepository;
import seniorproject.bankifycore.repository.RotationRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PartnerPortalService {

        private final ClientUserRepository clientUserRepo;
        private final RotationRepository rotationRepo;

        private UUID currentClientUserId() {
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                if (auth == null || !(auth.getPrincipal() instanceof UUID id)) {
                        throw new IllegalStateException("Partner portal request is not authenticated");
                }
                return id;
        }

        @Transactional(readOnly = true)
        public PartnerPortalMeResponse me() {
                UUID userId = currentClientUserId();

                ClientUser user = clientUserRepo.findById(userId)
                                .orElseThrow(() -> new IllegalStateException("Partner user not found"));

                var app = user.getClientApp();

                // docs metadata (static list for now)
                List<PartnerPortalMeResponse.PartnerEndpointDoc> docs = List.of(
                                new PartnerPortalMeResponse.PartnerEndpointDoc("GET", "/api/partner/me/balance",
                                                "Get settlement balance", false),
                                new PartnerPortalMeResponse.PartnerEndpointDoc("GET",
                                                "/api/partner/me/transactions?limit=...",
                                                "Latest transactions", false),
                                new PartnerPortalMeResponse.PartnerEndpointDoc("POST", "/api/partner/me/deposit",
                                                "Credit settlement account", true),
                                new PartnerPortalMeResponse.PartnerEndpointDoc("POST", "/api/partner/me/withdraw",
                                                "Debit settlement account", true),
                                new PartnerPortalMeResponse.PartnerEndpointDoc("POST", "/api/partner/me/transfer",
                                                "Transfer to another account", true));

                return new PartnerPortalMeResponse(
                                user.getId(),
                                user.getEmail(),
                                user.getRole().name(),
                                app.getId(),
                                app.getName(),
                                app.getStatus().name(),
                                app.getApiKeyHash() != null,
                                docs);
        }

        // rotation happens here , rotaion of key
        @Transactional
        public RotateKeyResponse requestRotation(RotateKeyRequest req) {
                UUID clientUserId = currentClientUserId();

                ClientUser user = clientUserRepo.findById(clientUserId)
                                .orElseThrow(() -> new IllegalStateException("Partner user not found"));

                ClientApp app = user.getClientApp();

                // Only ACTIVE apps can request rotation
                if (app.getStatus() != ClientStatus.ACTIVE) {
                        throw new IllegalStateException("Client app is not active");
                }

                // Prevent spam: only one pending request at a time
                if (rotationRepo.existsByClientApp_IdAndStatus(app.getId(), RotationStatus.PENDING)) {
                        throw new IllegalStateException("A rotation request is already pending");
                }

                ClientKeyRotationRequest r = ClientKeyRotationRequest.builder()
                                .clientApp(app)
                                .requestedBy(user)
                                .status(RotationStatus.PENDING)
                                .reason(req == null ? null : req.reason())
                                .build();

                var saved = rotationRepo.save(r);
                return new RotateKeyResponse(saved.getId(), saved.getStatus().name());
        }

        @Transactional(readOnly = true)
        public List<RotationRequestItem> myRotationRequests() {
                UUID userId = currentClientUserId();

                ClientUser user = clientUserRepo.findById(userId)
                                .orElseThrow(() -> new IllegalStateException("Partner user not found"));

                UUID appId = user.getClientApp().getId();

                return rotationRepo.findByClientApp_IdOrderByCreatedAtDesc(appId).stream()
                                .map(r -> new RotationRequestItem(
                                                r.getId(),
                                                r.getStatus().name(),
                                                r.getReason(),
                                                r.getCreatedAt()))
                                .toList();
        }

}
