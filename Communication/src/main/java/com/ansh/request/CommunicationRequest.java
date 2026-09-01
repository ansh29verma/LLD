package com.ansh.request;


import com.ansh.enums.ChannelType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class CommunicationRequest {
    private String requestId;
    private String accountId; // Used for routing (e.g. Critical OTP comms)

    public abstract ChannelType getChannelType();
    public abstract void validate();
}
