package com.sportseventmanagement.api

class API {


    companion object{
        val baseURl:String="https://sports-event-test.herokuapp.com/api"
        val docsURl:String="https://sports-event-test.herokuapp.com"


        val userLogin:String="$baseURl/user/login"
        val userRegister:String="$baseURl/user/register"
        val userGoogleLogin:String="$baseURl/user/google-login"
        val userFacebookLogin:String="$baseURl/user/facebook-login"
        val userNearByEvent:String="$baseURl/user/nearby-events"
        val userAllEventsMaker:String="$baseURl/user/event-makers"
        val userReportProblem:String="$baseURl/user/problem"
        val userUpdateUsername:String="$baseURl/user/username"
        val userUpdatePassword:String="$baseURl/user/update-password"
        val userEvents:String="$baseURl/user/events"
        val userNotification:String="$baseURl/user/notification"
        val userRecoveryEmail:String="$baseURl/user/password-recovery"
        val userRecoveryCode:String="$baseURl/user/verify-code"
        val userRestPass:String="$baseURl/user/reset-password"
        val userLogout:String="$baseURl/user/logout"
        val userAwards:String="$baseURl/user/awards"
        val userLeaderboard:String="$baseURl/user/leaderboard"
        val userStartRace:String="$baseURl/user/start-event"
    }
}