package smartassist.appreciate.be.smartassist.api;

import java.util.List;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Query;
import smartassist.appreciate.be.smartassist.model.api.AddContactsResponse;
import smartassist.appreciate.be.smartassist.model.api.ApiAccessToken;
import smartassist.appreciate.be.smartassist.model.api.ApiConfig;
import smartassist.appreciate.be.smartassist.model.api.CreateConversationContact;
import smartassist.appreciate.be.smartassist.model.api.CreateConversationResponse;
import smartassist.appreciate.be.smartassist.model.api.FlatId;
import smartassist.appreciate.be.smartassist.model.api.GetChatResponse;
import smartassist.appreciate.be.smartassist.model.api.LeaveConversationRequest;
import smartassist.appreciate.be.smartassist.model.api.LeaveConversationResponse;
import smartassist.appreciate.be.smartassist.model.api.MarkAsReadRequest;
import smartassist.appreciate.be.smartassist.model.api.MarkAsReadResponse;
import smartassist.appreciate.be.smartassist.model.api.SendMessageRequest;
import smartassist.appreciate.be.smartassist.model.api.SendMessageResponse;

/**
 * Created by Inneke on 28/01/2015.
 */
public interface SmartAssistApi
{
    @POST("/access-token")
    ApiAccessToken getAccessToken(@Body FlatId credentials,
                                  @Header("Application-Secret") String appSecret);

    @POST("/access-token")
    void getAccessToken(@Body FlatId credentials,
                        @Header("Application-Secret") String appSecret,
                        Callback<ApiAccessToken> callback);

    @GET("/flat")
    ApiConfig getFlatConfiguration(@Header("Application-Secret") String appSecret,
                                   @Header("Flat-Id") String flatId,
                                   @Header("Access-Token") String accessToken,
                                   @Header("OldSyncTimeStamp") String oldTimestamp,
                                   @Header("NewSyncTimeStamp") String newTimestamp,
                                   @Query("flat_id") int requestedFlatId);

    @GET("/messages")
    void getChat(@Header("Application-Secret") String appSecret,
                 @Header("Flat-ID") String flatId,
                 @Header("Access-Token") String accessToken,
                 @Header("OldSyncTimeStamp") String oldTimestamp,
                 @Header("NewSyncTimeStamp") String newTimestamp,
                 Callback<GetChatResponse> callback);

    @POST("/messages/new")
    SendMessageResponse sendMessage(@Header("Application-Secret") String appSecret,
                                    @Header("Flat-ID") String flatId,
                                    @Header("Access-Token") String accessToken,
                                    @Body SendMessageRequest request);

    @POST("/messages/read")
    MarkAsReadResponse markAsRead(@Header("Application-Secret") String appSecret,
                                  @Header("Flat-ID") String flatId,
                                  @Header("Access-Token") String accessToken,
                                  @Body MarkAsReadRequest request);

    @Multipart
    @POST("/conversations")
    CreateConversationResponse createConversation(@Header("Application-Secret") String appSecret,
                                                  @Header("Flat-ID") String flatId,
                                                  @Header("Access-Token") String accessToken,
                                                  @Part("contacts") List<CreateConversationContact> request);

    @POST("/conversations/leave")
    LeaveConversationResponse leaveConversation(@Header("Application-Secret") String appSecret,
                                                @Header("Flat-ID") String flatId,
                                                @Header("Access-Token") String accessToken,
                                                @Body LeaveConversationRequest request);

    @Multipart
    @POST("/conversations/contacts")
    AddContactsResponse addContacts(@Header("Application-Secret") String appSecret,
                                    @Header("Flat-ID") String flatId,
                                    @Header("Access-Token") String accessToken,
                                    @Part("conversation_id") int conversationId,
                                    @Part("contacts") List<CreateConversationContact> contacts);
}
