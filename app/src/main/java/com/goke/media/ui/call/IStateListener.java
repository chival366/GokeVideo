package com.goke.media.ui.call;

public interface IStateListener {
    int SdkCallback(int s, int c, String info, String jsonData);
}

enum SipEvent {
    /* REGISTER related events */
    SIP_REGISTRATION_SUCCESS,       /**< user is successfully registred.  */
    SIP_REGISTRATION_FAILURE,       /**< user is not registred.           */

    /* INVITE related events within calls */
    SIP_CALL_INVITE,            /**< announce a new call                   */
    SIP_CALL_REINVITE,          /**< announce a new INVITE within call     */

    SIP_CALL_NOANSWER,          /**< announce no answer within the timeout */
    SIP_CALL_PROCEEDING,        /**< announce processing by a remote app   */
    SIP_CALL_RINGING,           /**< announce ringback                     */
    SIP_CALL_ANSWERED,          /**< announce start of call                */
    SIP_CALL_REDIRECTED,        /**< announce a redirection                */
    SIP_CALL_REQUESTFAILURE,    /**< announce a request failure            */
    SIP_CALL_SERVERFAILURE,     /**< announce a server failure             */
    SIP_CALL_GLOBALFAILURE,     /**< announce a global failure             */
    SIP_CALL_ACK,               /**< ACK received for 200ok to INVITE      */

    SIP_CALL_CANCELLED,         /**< announce that call has been cancelled */

    /* request related events within calls (except INVITE) */
    SIP_CALL_MESSAGE_NEW,              /**< announce new incoming request. */
    SIP_CALL_MESSAGE_PROCEEDING,       /**< announce a 1xx for request. */
    SIP_CALL_MESSAGE_ANSWERED,         /**< announce a 200ok  */
    SIP_CALL_MESSAGE_REDIRECTED,       /**< announce a failure. */
    SIP_CALL_MESSAGE_REQUESTFAILURE,   /**< announce a failure. */
    SIP_CALL_MESSAGE_SERVERFAILURE,    /**< announce a failure. */
    SIP_CALL_MESSAGE_GLOBALFAILURE,    /**< announce a failure. */

    SIP_CALL_CLOSED,            /**< a BYE was received for this call      */

    /* for both UAS & UAC events */
    SIP_CALL_RELEASED,             /**< call context is cleared.            */

    /* events received for request outside calls */
    SIP_MESSAGE_NEW,              /**< announce new incoming request. */
    SIP_MESSAGE_PROCEEDING,       /**< announce a 1xx for request. */
    SIP_MESSAGE_ANSWERED,         /**< announce a 200ok  */
    SIP_MESSAGE_REDIRECTED,       /**< announce a failure. */
    SIP_MESSAGE_REQUESTFAILURE,   /**< announce a failure. */
    SIP_MESSAGE_SERVERFAILURE,    /**< announce a failure. */
    SIP_MESSAGE_GLOBALFAILURE,    /**< announce a failure. */

    /* Presence and Instant Messaging */
    SIP_SUBSCRIPTION_NOANSWER,          /**< announce no answer              */
    SIP_SUBSCRIPTION_PROCEEDING,        /**< announce a 1xx                  */
    SIP_SUBSCRIPTION_ANSWERED,          /**< announce a 200ok                */
    SIP_SUBSCRIPTION_REDIRECTED,        /**< announce a redirection          */
    SIP_SUBSCRIPTION_REQUESTFAILURE,    /**< announce a request failure      */
    SIP_SUBSCRIPTION_SERVERFAILURE,     /**< announce a server failure       */
    SIP_SUBSCRIPTION_GLOBALFAILURE,     /**< announce a global failure       */
    SIP_SUBSCRIPTION_NOTIFY,            /**< announce new NOTIFY request     */

    SIP_IN_SUBSCRIPTION_NEW,            /**< announce new incoming SUBSCRIBE/REFER.*/

    SIP_NOTIFICATION_NOANSWER,          /**< announce no answer              */
    SIP_NOTIFICATION_PROCEEDING,        /**< announce a 1xx                  */
    SIP_NOTIFICATION_ANSWERED,          /**< announce a 200ok                */
    SIP_NOTIFICATION_REDIRECTED,        /**< announce a redirection          */
    SIP_NOTIFICATION_REQUESTFAILURE,    /**< announce a request failure      */
    SIP_NOTIFICATION_SERVERFAILURE,     /**< announce a server failure       */
    SIP_NOTIFICATION_GLOBALFAILURE,     /**< announce a global failure       */

    SIP_EVENT_COUNT                  /**< MAX number of events              */
}