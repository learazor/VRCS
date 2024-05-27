package org.example.viewmodel.login;

import org.example.model.client.Client;
import org.example.model.client.ClientType;
import org.example.repository.ClientRepository;

import java.util.Map;
import java.util.prefs.Preferences;

public class LoginViewModel {
    private final Map<String, Client> clients;
    private final ClientRepository clientRepository;

    public LoginViewModel() {

        clientRepository = new ClientRepository();
        clients = prepareClientsHashMap(clientRepository);
    }

    private Map<String, Client> prepareClientsHashMap(ClientRepository clientRepository) {
        return clientRepository.prepareClients();
    }

    public boolean authenticate(String phone, String password) {
        Client client = clients.get(phone);

        if (client != null && client.password().equals(password)) {
            Preferences userPreferences = Preferences.userRoot();
            userPreferences.put("phone", phone);
            return true;
        } else {
            return false;
        }

    }

    public boolean isAdmin(String phone) {
        Client client = clients.get(phone);
        return client != null && client.type() == ClientType.ADMIN;
    }

    public void signUp(String phone, String password, String clientType) {
        ClientType type = ClientType.valueOf(clientType.toUpperCase());
        Client client = new Client(phone, password, ClientType.valueOf(clientType));
        clientRepository.saveClient(client);
        clients.put(phone, new Client(phone, password, type));
    }
}
