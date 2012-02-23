/*************************************************************************************************
 * 
 * @author : 姚武平
 * @since : JDK 1.4
 * @date : 2009-10-8
 * @version : 1.2
 * @description : 基类的报文类型
 * 
 *************************************************************************************************/

package scu.im.msgtype;

import java.io.Serializable;

public class Msg implements Serializable {

	private static final long serialVersionUID = -2923417251997247959L;
	private Option head;
	private Object body;

	public Option getHead() {
		return head;
	}

	public void setHead(Option head) {
		this.head = head;
	}

	public Object getBody() {
		return body;
	}

	public void setBody(Object body) {
		this.body = body;
	}
}
