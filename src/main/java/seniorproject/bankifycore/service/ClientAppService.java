package seniorproject.bankifycore.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import seniorproject.bankifycore.domain.ClientApp;
import seniorproject.bankifycore.domain.enums.ClientStatus;
import seniorproject.bankifycore.dto.clientapp.ClientAppResponse;
import seniorproject.bankifycore.dto.clientapp.CreateClientAppRequest;
import seniorproject.bankifycore.dto.clientapp.CreateClientAppResponse;
import seniorproject.bankifycore.repository.ClientAppRepository;
import seniorproject.bankifycore.utils.ApiKeyUtils;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClientAppService {

    private final ClientAppRepository clientAppRepo;

    @Value("${security.api-key.pepper:change-me}")
    private String pepper;

    @Transactional
    public CreateClientAppResponse create(CreateClientAppRequest req) {
        if (req.name() == null || req.name().isBlank()) {
            throw new IllegalArgumentException("Client name is required");
        }

        String rawKey = ApiKeyUtils.generateRawKey();
        String hash = ApiKeyUtils.hash(rawKey, pepper);

        // very unlikey but, keep it correct
        while (clientAppRepo.existsByApiKeyHash(hash)) {
            rawKey = ApiKeyUtils.generateRawKey();
            hash = ApiKeyUtils.hash(rawKey, pepper);
        }

        ClientApp client = ClientApp.builder()
                .name(req.name())
                .status(ClientStatus.ACTIVE)
                .apiKeyHash(hash)
                .build();

        ClientApp saved = clientAppRepo.save(client);

        // return the raw key ONCE
        return new CreateClientAppResponse(
                saved.getId(),
                saved.getName(),
                saved.getStatus().name(),
                rawKey);
    }

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

        return toClientAppResponse(client);
    }

    private ClientAppResponse toClientAppResponse(ClientApp client) {
        return new ClientAppResponse(
                client.getId(),
                client.getName(),
                client.getStatus().name(),
                client.getCreatedAt());
    }

}
