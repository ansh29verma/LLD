package com.ansh;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.



import com.ansh.client.ExternalProviderClient;
import com.ansh.client.ProviderClient;
import com.ansh.enums.ChannelType;
import com.ansh.model.Account;
import com.ansh.model.AuthCredentials;
import com.ansh.model.Provider;
import com.ansh.repository.InMemoryCommunicationLogRepository;
import com.ansh.repository.InMemoryProviderRepository;
import com.ansh.repository.ProviderRepository;
import com.ansh.request.EmailRequest;
import com.ansh.request.SMSRequest;
import com.ansh.service.CommunicationService;
import com.ansh.service.CommunicationServiceImpl;
import com.ansh.strategy.RandomProviderSelectionStrategy;

import java.util.Map;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        // Instantiate Dependencies
        ProviderRepository providerRepo = new InMemoryProviderRepository();
        InMemoryCommunicationLogRepository logRepo = new InMemoryCommunicationLogRepository();
        ProviderClient providerClient = new ExternalProviderClient();
        RandomProviderSelectionStrategy strategy = new RandomProviderSelectionStrategy();

        CommunicationService communicationService = new CommunicationServiceImpl(providerRepo, logRepo, providerClient, strategy);

        // 1. Onboard Account Configurations
        Account otpAccount = new Account("ACC_OTP", "OTP Account", Set.of(ChannelType.SMS, ChannelType.EMAIL), true);
        Account promoAccount = new Account("ACC_PROMO", "Promotional Account", Set.of(ChannelType.SMS), true);

        // 2. Onboard Provider 1 (Twilio)
        Provider p1 = Provider.builder()
                .id("P1")
                .name("Twilio")
                .active(true)
                .credentials(new AuthCredentials("twilio_user", "pass123", null))
                .endpoints(Map.of(
                        ChannelType.SMS, "https://api.twilio.com/v1/sms",
                        ChannelType.EMAIL, "https://api.twilio.com/v1/email"
                ))
                .accounts(Map.of("ACC_OTP", otpAccount))
                .build();

        // 3. Onboard Provider 2 (AWS Pinpoint)
        Provider p2 = Provider.builder()
                .id("P2")
                .name("AWS_Pinpoint")
                .active(true)
                .credentials(new AuthCredentials("aws_key", "aws_secret", null))
                .endpoints(Map.of(
                        ChannelType.SMS, "https://pinpoint.amazonaws.com/v1/sms"
                ))
                .accounts(Map.of("ACC_OTP", otpAccount, "ACC_PROMO", promoAccount))
                .build();

        // 4. Save Providers
        communicationService.addProvider(p1);
        communicationService.addProvider(p2);

        // 5. Execute Request 1: Critical OTP SMS
        SMSRequest smsRequest = new SMSRequest();
        smsRequest.setRequestId("REQ_001");
        smsRequest.setAccountId("ACC_OTP");
        smsRequest.setMobileNumber("+1234567890");
        smsRequest.setMessage("Your OTP is 492011");

        System.out.println("--- Executing SMS Request ---");
        communicationService.processRequest(smsRequest);

        // 6. Execute Request 2: Email
        EmailRequest emailRequest = new EmailRequest();
        emailRequest.setRequestId("REQ_002");
        emailRequest.setAccountId("ACC_OTP");
        emailRequest.setSender("no-reply@company.com");
        emailRequest.setReceiver("user@example.com");
        emailRequest.setSubject("Welcome Onboard!");
        emailRequest.setMessage("Thank you for registering.");

        System.out.println("\n--- Executing Email Request ---");
        communicationService.processRequest(emailRequest);

        // 7. Process Callback from Provider
        System.out.println("\n--- Processing Provider Webhook Callback ---");
        communicationService.handleCallback("REQ_001", "DELIVERED");
    }
}