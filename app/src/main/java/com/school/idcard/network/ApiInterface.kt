package com.school.idcard.network

import com.school.idcard.admin.RoleResponse
import com.school.idcard.othermodel.AddSchoolRequest
import com.school.idcard.othermodel.AgentResponse
import com.school.idcard.othermodel.AgentResponse2
import com.school.idcard.othermodel.GetSchoolResponse
import com.school.idcard.othermodel.LoginResponse
import com.school.idcard.othermodel.ProfileResponse
import com.school.idcard.othermodel.SchoolResponse
import com.school.idcard.othermodel.SchoolsResponse2
import com.school.idcard.superadmin.model.AgentListModel
import com.school.idcard.superadmin.model.StaffResponse
import com.school.idcard.superadmin.model.StudentSuperAdminModel
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInterface {

    @POST("login/all")
    @Headers("Accept:application/json", "Content-Type:application/json")
    fun loginUser(@Body request: LoginRequest): Call<LoginResponse>


    //start Agent
    @POST("agent/add")
    fun addAgent(
        @Header("Authorization") token: String,
        @Query("firstName") firstName: String,
        @Query("lastName") lastName: String,
        @Query("agentEmail") agentEmail: String,
        @Query("contactNo") contactNo: String,
        @Query("agentType") agentType: String,
        @Query("address1") address1: String,
        @Query("address2") address2: String,
        @Query("city") city: String,
        @Query("pinCode") pinCode: String,
        @Query("state") state: String,
        @Query("country") country: String,
        @Query("startSubs") startSubs: String,
        @Query("endSubs") endSubs: String,
        @Query("status") status: String,
    ): Call<CommonResponse>

    @GET("agent/getAllAgent")
    fun getAgent(@Header("Authorization") token: String, @Query("status")status: String): Call<AgentListModel>

    @DELETE("agent/deleteById/{id}")
    fun deleteAgent(
        @Header("Authorization") token: String,
        @Path("id") agentId: String
    ): Call<CommonResponse>

    @GET("agent/getById/{id}")
    fun getAgentById(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Call<AgentResponse>

    @PUT("agent/update/{id}")
    fun updateAgent(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Query("firstName") firstName: String,
        @Query("lastName") lastName: String,
        @Query("agentEmail") agentEmail: String,
        @Query("contactNo") contactNo: String,
        @Query("agentType") agentType: String,
        @Query("address1") address1: String,
        @Query("address2") address2: String,
        @Query("city") city: String,
        @Query("pinCode") pinCode: String,
        @Query("state") state: String,
        @Query("country") country: String,
        @Query("startSubs") startSubs: String,
        @Query("endSubs") endSubs: String,
        @Query("status") status: String,
    ): Call<CommonResponse>

    //end Agent

    //start school

    @POST("school/add")
    fun addSchool(
        @Header("Authorization") authorization: String,
        @Body requestBody: AddSchoolRequest
    ): Call<CommonResponse>

    @PUT("school/update/{id}")
    fun updateSchool(
        @Header("Authorization") authorization: String,
        @Body requestBody: AddSchoolRequest,
        @Path ("id") id: String
    ): Call<CommonResponse>


    @GET("agent/getAllLight")
    fun getAgentList(
        @Header("Authorization") authorization: String
    ): Call<AgentResponse2>

    @GET("school/getSchoolLight")
    fun getSchoolList(
        @Header("Authorization") authorization: String
    ): Call<SchoolsResponse2>

    @GET("school/getAll")
    fun getSchool(@Header("Authorization") token: String, @Query("searchKey")searchKey:String): Call<SchoolResponse>

    @GET("student/getBySchool/{id}")
    fun getSuperAdminStudentList(
        @Header("Authorization") authorization: String,
        @Path("id")id: String
    ): Call<StudentSuperAdminModel>

    @GET("staff/getBySchool/{id}")
    fun getSuperAdminStaffList(
        @Header("Authorization") authorization: String,
        @Path("id")id: String
    ): Call<StaffResponse>

    @DELETE("staff/deleteById/{id}")
    fun deleteStaff(
        @Header("Authorization") authorization: String,
        @Path("id")id: String
    ): Call<CommonResponse>

    @DELETE("student/deleteStudent/{id}")
    fun deleteStudent(
        @Header("Authorization") authorization: String,
        @Path("id")id: String
    ): Call<CommonResponse>

    //end School

    @POST("login/changeMyPassword")
    fun changePassword(
        @Header("Authorization") authorization: String,
        @Query ("oldPassword")oldPassword: String,
        @Query ("newPassword")newPassword: String
    ):Call<CommonResponse>

    @GET("login/getProfile")
    fun getProfile(
        @Header("Authorization") authorization: String,
    ):Call<ProfileResponse>

    @GET("school/getById/{id}")
    fun getSchoolViaId(
        @Header("Authorization") authorization: String,
        @Path("id") id: String,
    ):Call<GetSchoolResponse>

    @DELETE("school/deleteById/{id}")
    fun deleteSchool(
        @Header("Authorization") token: String,
        @Path("id") schoolId: String
    ): Call<CommonResponse>

    @PUT("agent/updateByToken")
    fun updateAgentProfile(
        @Header("Authorization") token:String,
        @Query("firstName")firstName:String,
        @Query("lastName")lastName:String,
        @Query("contactNo")contactNo:String,
        @Query("address1")address1:String,
        @Query("state")state: String,
        @Query("city")city: String,
        @Query("pinCode")pinCode: String,
        @Query("country")country: String,
    ):Call<CommonResponse>



    //Admin Panel Related Api

    @POST("role/add")
    fun addRole(
        @Header("Authorization") authorization: String,
        @Query("roleName") roleName:String
    ):Call<CommonResponse>

    @GET("role/getAll")
    fun getAllRoles(@Header("Authorization") token: String): Call<RoleResponse>

    @POST("staff/add")
    fun addStaff(
        @Header("Authorization") authorization: String,
        @Query("staffName") staffName:String,
        @Query("contactNo") contactNo:String,
        @Query("address1") address1:String,
        @Query("address2") address2:String,
        @Query("city") city:String,
        @Query("pinCode") pinCode:String,
        @Query("state") state:String,
        @Query("country") country:String,
        @Query("emailId") emailId:String,
        @Query("roleType") roleType:String,
        @Part photo: MultipartBody.Part?
    ):Call<CommonResponse>


}
