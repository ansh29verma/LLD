package com.ansh.model;
import com.ansh.enums.ChannelType;
import com.ansh.enums.RequestStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommunicationLog {
    private String requestId;
    private String providerId;
    private ChannelType channelType;
    private RequestStatus status;
    private String accountId;
}
