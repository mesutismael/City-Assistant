package smartassist.appreciate.be.smartassist.utils;

import smartassist.appreciate.be.smartassist.R;

/**
 * Created by Inneke De Clippel on 18/04/2016.
 */
public class ChatHelper
{
    public static final int STATE_SENT = 0;
    public static final int STATE_ERROR = 1;
    public static final int STATE_SENDING = 2;

    public static int getMessageForState(int state)
    {
        switch (state)
        {
            case STATE_SENDING:
                return R.string.chat_state_sending;

            case STATE_ERROR:
                return R.string.chat_state_error;

            default:
                return 0;
        }
    }

    public static boolean shouldShowMessage(int state)
    {
        switch (state)
        {
            case STATE_SENDING:
                return true;

            case STATE_ERROR:
                return true;

            default:
                return false;
        }
    }
}
