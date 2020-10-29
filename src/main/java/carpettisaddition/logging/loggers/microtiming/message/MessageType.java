package carpettisaddition.logging.loggers.microtiming.message;

import carpettisaddition.logging.loggers.microtiming.enums.EventType;

public enum MessageType
{
	ATOM,
	PROCEDURE;

	public static MessageType fromEventType(EventType eventType)
	{
		switch (eventType)
		{
			case EVENT:
			case ACTION:
				return ATOM;
			case ACTION_START:
			case ACTION_END:
				return PROCEDURE;
			default:
				return null;
		}
	}
}
