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
public class EmailRequest extends CommunicationRequest {
    private String sender;
    private String receiver;
    private String subject;
    private String message;

    @Override
    public ChannelType getChannelType() {
        return ChannelType.EMAIL;
    }

    @Override
    public void validate() {
        if (sender == null || sender.isBlank() ||
                receiver == null || receiver.isBlank() ||
                subject == null || subject.isBlank() ||
                message == null || message.isBlank()) {
            throw new CommunicationException(ErrorCode.INVALID_REQUEST, "Email request requires sender, receiver, subject, and message.");
        }
    }
}