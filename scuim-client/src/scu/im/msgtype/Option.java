package scu.im.msgtype;

public enum Option {
	EXIT, 						//index: 0
	LOGIN, 						//index: 1
	LOGIN_RETURN, 				//index: 2
	FRIENDLIST, 				//index: 3
	FRIENDLOGIN, 				//index: 4
	MESSAGE_WITH_ATTRIB, 		//index: 5
	TCP_FILE_REQUEST_TYPE,		//index: 6
	TCP_FILE_RESPONSE_TYPE,		//index: 7
	SEARCH_USER_TYPE,       	//index: 8
	ADD_FRIEND_TYPE,			//index: 9
	CONFIRM_RETURN_TYPE,		//index: 10
	CONFIRM_RESULT_TYPE,    	//index: 11
	GROUP_UNIT_TYPE, 			//index: 12
	GROUP_MESSAGE_TYPE,     	//index: 13
	SEARCH_GROUP_TYPE,      	//index: 14
	SEARCH_GROUP_RETURN_TYPE, 	//index: 15
	GROUP_APPLY_TYPE,			//index: 16
	GROUP_RESULT_TYPE,			//index: 17
	GROUP_INFO_REQUEST, 		//index: 18
	GROUP_INFO_RESPONSE, 		//index: 19
	USER_INFO_REQUEST,			//index: 20
	USER_INFO_RESPONSE,			//index: 21
	SUBMIT_USER_INFO_TYPE,		//index: 22
	USER_LEAVE,					//index：23
	MSG_REPLY,					//index: 24
	VOICE_CHAT_REQUEST,			//index: 25
	VOICE_CHAT_RESPONSE,		//index: 26
	SUBMIT_GROUP_INFO_TYPE,		//index: 27
	STOP_VOICE_TYPE,			//index: 28 没有具体类型
	STOP_VOICE_RESPONSE_TYPE,	//index: 29 没有具体类型
}
