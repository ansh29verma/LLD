package com.ansh.request;


import com.ansh.enums.ChannelType;
import com.ansh.enums.ErrorCode;
import com.ansh.exceptions.CommunicationException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class SoundBoxRequest extends CommunicationRequest {
    private String deviceId;
    private String amount;
    private String audioMessage;

    @Override
    public ChannelType getChannelType() {
        return ChannelType.SOUNDBOX;
    }

    @Override
    public void validate() {
        if (deviceId == null || deviceId.isBlank() || audioMessage == null || audioMessage.isBlank()) {
            throw new CommunicationException(ErrorCode.INVALID_REQUEST, "Soundbox request requires deviceId and audioMessage.");
        }
    }
}