import java.io.Serializable;
import java.util.Date;

/* Class Mail
 * Encapsulation des informations liées à un mail
 * @author Djothi
 *
 */
public class Mail implements Serializable
{
	private Date date;
	private String correspondant;
	private String message;
	
	public Mail(Date dte, String exp, String msg)
	{
		this.date = dte;
		this.correspondant = exp;
		this.message = msg;
	}
	
	public Date getDate()
	{
		return this.date;
	}
	
	public String getCorrespondant()
	{
		return this.correspondant;
	}
	
	public String getMessage()
	{
		return this.message;
	}
}
