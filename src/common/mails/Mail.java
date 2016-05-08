package common.mails;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Bruno Buiret (bruno.buiret@etu.univ-lyon1.fr)
 * @author Thomas Arnaud (thomas.arnaud@etu.univ-lyon1.fr)
 * @author Alexis Rabilloud (alexis.rabilloud@etu.univ-lyon1.fr)
 */
public class Mail
{
    /**
     *
     */
    protected static final Pattern PATTERN_MAIL = Pattern.compile(
        "^((?:(?:[^:\\n\\r]+):(?:[^:]+)\\r\\n)+)\\r\\n(.+)\\r\\n\\.\\r\\n",
        Pattern.MULTILINE
    );

    /**
     *
     */
    protected Map<String, String> headers;

    /**
     *
     */
    protected String body;

    /**
     *
     */
    public Mail()
    {
        this.headers = new HashMap<>();
        this.body = null;
    }

    /**
     *
     * @param data
     * @return
     * @todo Find a way to get rid of the unwanted line breaks in the body.
     */
    public static Mail parse(String data)
    {
        // Initialize vars
        StringBuilder bodyBuilder = new StringBuilder();
        Mail mail = new Mail();
        Matcher matcher = Mail.PATTERN_MAIL.matcher(data);
        
        if(matcher.matches())
        {
            // There are headers and a body
            String[] headersLines = matcher.group(1).split("\r\n");
            
            for(String headerLine : headersLines)
            {
                mail.addHeader(headerLine);
            }
            
            // Add body
            mail.setBody(matcher.group(2));
        }
        else
        {
            // There is only the body
            mail.setBody(data);
        }
        
        return mail;
    }

    /**
     *
     * @param name
     * @return
     */
    public String getHeader(String name)
    {
        return this.headers.getOrDefault(name, null);
    }

    /**
     *
     * @return
     */
    public Map<String, String> getHeaders()
    {
        return this.headers;
    }

    /**
     *
     * @param header
     */
    public void addHeader(String header)
    {
        int colonPos = header.indexOf(":");

        if(-1 != colonPos)
        {
            this.headers.put(header.substring(0, colonPos), header.substring(colonPos + 2));
        }
        else
        {
            throw new IllegalArgumentException(String.format(
                "Malformed header \"%s\".",
                header
            ));
        }
    }

    /**
     *
     * @param name
     * @param value
     */
    public void addHeader(String name, String value)
    {
        this.headers.put(name, value);
    }

    /**
     *
     * @return
     */
    public String getBody()
    {
        return this.body;
    }

    /**
     *
     * @param body
     */
    public void setBody(String body)
    {
        this.body = body;
    }

    /**
     *
     * @return
     */
    public int getSize()
    {
        return this.getSize(StandardCharsets.UTF_8);
    }

    /**
     *
     * @param charset
     * @return
     * @todo
     */
    public int getSize(Charset charset)
    {
        return 0;
    }

    /**
     *
     * @return
     */
    public byte[] getBytes()
    {
        return this.getBytes(StandardCharsets.UTF_8);
    }

    /**
     *
     * @param charset
     * @return
     * @todo
     */
    public byte[] getBytes(Charset charset)
    {
        return new byte[]{};
    }
}
