package smtp.server.commands;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import pop3.Pop3Protocol;
import smtp.server.SmtpConnection;
import smtp.server.SmtpState;

/**
 * @author Bruno Buiret (bruno.buiret@etu.univ-lyon1.fr)
 * @author Thomas Arnaud (thomas.arnaud@etu.univ-lyon1.fr)
 * @author Alexis Rabilloud (alexis.rabilloud@etu.univ-lyon1.fr)
 */
public class RecipientCommand extends AbstractSmtpCommand
{
    /**
     * The command pattern to fetch recipients for this transaction.
     * 
     * @see http://emailregex.com/
     */
    protected static final Pattern COMMAND_PATTERN = Pattern.compile(
        "RCPT TO:<((?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\]))>"
    );
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isValid(SmtpConnection connection)
    {
        return connection.getCurrentState().equals(SmtpState.EXPECTING_RECIPIENTS);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean handle(SmtpConnection connection, String request)
    {
        // Initialize vars
        StringBuilder responseBuilder = new StringBuilder();
        
        // Is the syntax valid?
        if(request.startsWith("RCPT TO:"))
        {
            // Has the recipient's email been given?
            Matcher matcher = MailCommand.COMMAND_PATTERN.matcher(request);

            if(matcher.matches())
            {
                // Store the recipient in the associated buffer
                Set<String> recipientsBuffer = connection.getRecipientsBuffer();
                
                if(!recipientsBuffer.contains(matcher.group(1)))
                {
                    recipientsBuffer.add(matcher.group(1));
                }
                else
                {
                    
                }
                
                // Build response
                responseBuilder.append("250 OK");
                responseBuilder.append(Pop3Protocol.END_OF_LINE);
            }
            else
            {
                // Inform the user the email is invalid
                responseBuilder.append("501 Syntax error in parameters or arguments");
                responseBuilder.append(Pop3Protocol.END_OF_LINE);
            }
        }
        else
        {
            // Inform the user the syntax is incorrect
            responseBuilder.append("501 Syntax error in parameters or arguments");
            responseBuilder.append(Pop3Protocol.END_OF_LINE);
        }
        
        return true;
    }
}
