package seniorproject.bankifycore.service.partner;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import seniorproject.bankifycore.domain.Account;
import seniorproject.bankifycore.domain.ClientApp;
import seniorproject.bankifycore.domain.ClientKeyRotationRequest;
import seniorproject.bankifycore.domain.enums.*;
import seniorproject.bankifycore.dto.ApproveRotationResponse;
import seniorproject.bankifycore.dto.RejectRotationResponse;
import seniorproject.bankifycore.dto.admin.ApproveClientResponse;
import seniorproject.bankifycore.dto.clientapp.ClientAppResponse;
import seniorproject.bankifycore.repository.AccountRepository;
import seniorproject.bankifycore.repository.ClientAppRepository;
import seniorproject.bankifycore.repository.RotationRepository;
import seniorproject.bankifycore.service.AccountService;
import seniorproject.bankifycore.service.AuditService;
import seniorproject.bankifycore.utils.ActorContext;
import seniorproject.bankifycore.utils.ApiKeyUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClientAppService {

    private final ClientAppRepository clientAppRepo;
    private final AccountRepository accountRepo;
    private final AccountService accountService;
    private final RotationRepository rotationRepo;
    private final AuditService auditService;


    @Value("${security.api-key.pepper:change-me}")
    private String pepper;

    // @Transactional
    // public CreateClientAppResponse create(CreateClientAppRequest req) {
    // if (req.name() == null || req.name().isBlank()) {
    // throw new IllegalArgumentException("Client name is required");
    // }

    // String rawKey = ApiKeyUtils.generateRawKey();
    // String hash = ApiKeyUtils.hash(rawKey, pepper);

    // // very unlikey but, keep it correct
    // while (clientAppRepo.existsByApiKeyHash(hash)) {
    // rawKey = ApiKeyUtils.generateRawKey();
    // hash = ApiKeyUtils.hash(rawKey, pepper);
    // }

    // ClientApp client = ClientApp.builder()
    // .name(req.name().trim())
    // .status(ClientStatus.ACTIVE)
    // .apiKeyHash(hash)
    // .build();

    // ClientApp saved = clientAppRepo.save(client);

    // Account partnerAccount = Account.builder()
    // .customer(null) // or however your Account is modeled (partner accounts don't
    // belong to
    // // Customer)
    // .type(AccountType.CURRENT) // banking type
    // .currency(Currency.THB) //
    // .status(AccountStatus.ACTIVE)
    // .balance(BigDecimal.ZERO)
    // .accountNumber(accountService.getUniqueAccountNumber()) // reuse the existing
    // accoutn no generator
    // .clientApp(saved) // ✅ link
    // .build();

    // accountRepo.save(partnerAccount);

    // // return the raw key ONCE
    // return new CreateClientAppResponse(
    // saved.getId(),
    // saved.getName(),
    // saved.getStatus().name(),
    // rawKey);
    // }

    @Transactional
    public List<ClientAppResponse> list() {
        return clientAppRepo.findAll().stream()
                .map(this::toClientAppResponse)
                .toList();
    }

    @Transactional
    public ClientAppResponse disable(UUID id) {
        ClientApp client = clientAppRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ClientApp not found"));

        client.setStatus(ClientStatus.DISABLED);
        clientAppRepo.save(client);


        //audit log is generated here
        auditService.log(
                ActorContext.actorType(), ActorContext.actorId(),
                "CLIENT_DISABLED",
                "ClientApp", client.getId().toString(),
                "status=" + client.getStatus().name()
        );


        return toClientAppResponse(client);
    }

    @Transactional
    public ApproveClientResponse approve(UUID clientAppId) {
        ClientApp app = clientAppRepo.findById(clientAppId)
                .orElseThrow(() -> new IllegalArgumentException("Client not found"));

        if (app.getStatus() != ClientStatus.PENDING) {
            throw new IllegalStateException("Client is not pending");
        }

        if (app.getApiKeyHash() != null) {
            throw new IllegalStateException("API key already issued");
        }

        // generate key ONCE (store only hash)
        String apiKeyPlain, apiKeyHash;
        do {
            apiKeyPlain = ApiKeyUtils.generateRawKey();
            apiKeyHash = ApiKeyUtils.hash(apiKeyPlain, pepper);
        } while (clientAppRepo.existsByApiKeyHash(apiKeyHash));

        app.setApiKeyHash(apiKeyHash);
        app.setStatus(ClientStatus.ACTIVE);

        // create settlement account if missing
        if (!accountRepo.existsByClientApp_Id(app.getId())) {
            Account settlement = Account.builder()
                    .clientApp(app)
                    .customer(null)
                    .type(AccountType.CURRENT)
                    .status(AccountStatus.ACTIVE)
                    .accountNumber(accountService.getUniqueAccountNumber())
                    .currency(Currency.THB) // or whatever default you want
                    .balance(BigDecimal.ZERO)
                    .build();
            accountRepo.save(settlement);
        }

        clientAppRepo.save(app);

        //audit log is generated here
        auditService.log(
                ActorContext.actorType(), ActorContext.actorId(),
                "CLIENT_APPROVED",
                "ClientApp", app.getId().toString(),
                "status=" + app.getStatus().name()
        );

        return new ApproveClientResponse(app.getId(), app.getStatus().name(), apiKeyPlain);
    }

    //Here is the method that will approve , Rotation for client
    @Transactional
    public ApproveRotationResponse approveRotation(UUID requestId) {
        ClientKeyRotationRequest r = rotationRepo.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Rotation request not found"));

        if (r.getStatus() != RotationStatus.PENDING) {
            throw new IllegalStateException("Rotation request is not pending");
        }

        ClientApp app = r.getClientApp();
        if (app.getStatus() != ClientStatus.ACTIVE) {
            throw new IllegalStateException("Client app is not active");
        }

        // Generate new key (plain once), store hash
        String apiKeyPlain, apiKeyHash;
        do {
            apiKeyPlain = ApiKeyUtils.generateRawKey();
            apiKeyHash = ApiKeyUtils.hash(apiKeyPlain, pepper);
        } while (clientAppRepo.existsByApiKeyHash(apiKeyHash));

        app.setApiKeyHash(apiKeyHash);
        clientAppRepo.save(app);

        r.setStatus(RotationStatus.APPROVED);
        rotationRepo.save(r);

        return new ApproveRotationResponse(r.getId(), app.getId(), r.getStatus().name(), apiKeyPlain);
    }

    @Transactional
    public RejectRotationResponse reject(UUID requestId) {
        ClientKeyRotationRequest r = rotationRepo.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Rotation request not found"));

        if (r.getStatus() != RotationStatus.PENDING) {
            throw new IllegalStateException("Rotation request is not pending");
        }

        r.setStatus(RotationStatus.REJECTED);
        rotationRepo.save(r);
        return new RejectRotationResponse(r.getId(), r.getStatus().name());
    }







    private ClientAppResponse toClientAppResponse(ClientApp client) {
        return new ClientAppResponse(
                client.getId(),
                client.getName(),
                client.getStatus().name(),
                client.getCreatedAt());
    }

}
