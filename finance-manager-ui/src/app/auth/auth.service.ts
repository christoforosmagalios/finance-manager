import { HttpClient } from '@angular/common/http';
import { Injectable } from "@angular/core";
import { Router } from '@angular/router';
import { BehaviorSubject } from 'rxjs';
import { map } from 'rxjs/operators';
import { AuthDTO } from '../dto/auth-dto';
import { ForgotPasswordDTO } from '../dto/forgot-password-dto';
import { LoginDTO } from '../dto/login-dto';
import { ResetPasswordDTO } from '../dto/reset-password-dto';
import { UserDetailsDTO } from '../dto/user-details-dto';
import { Constants } from '../shared/constants/constants';
import { MessageService } from '../shared/socket/message.service';

@Injectable({providedIn: 'root'})
export class AuthService {
    private endpoint = Constants.ENTITY.AUTH;
    user = new BehaviorSubject<AuthDTO>(null);

    constructor(
        private http: HttpClient,
        private router: Router,
        private messageService: MessageService) {}

    /**
     * Register the user with the given information.
     * 
     * @param userDetailsDTO Object with the registration info.
     */
    register(userDetailsDTO: UserDetailsDTO) {
        return this.http.post(Constants.API + '/' + this.endpoint + '/register', userDetailsDTO);
    }

    /**
     * Login with the given credentials.
     * 
     * @param loginDTO Object with the username/password info.
     */
    login(loginDTO: LoginDTO) {
        return this.http.post(Constants.API + '/login', loginDTO, {observe: 'response'}).pipe(map(res => {
            this.handleAuthentication(res.headers.get(Constants.HEADERS.AUTH_USER), res.headers.get(Constants.HEADERS.AUTH));
        }));
    }

    /**
     * After a successful authentication, update the logged in user info, and
     * store the session info in the browsers local storage.
     * 
     * @param fullname The full name of the logged in user.
     * @param token The token of the logged in user.
     */
    private handleAuthentication(fullname:string, token: string) {
        // Init a Auth object.
        const user = new AuthDTO(fullname, token);
        // Update the logged in user info.
        this.user.next(user);
        // Store the session info in the local storage.
        localStorage.setItem(Constants.LOCAL_STORAGE.SESSION, JSON.stringify(user));
        // Connect to the WebSocket.
        this.messageService.connect();
    }

    /**
     * Delete the token stored in the local storage and redirect to the Login page.
     */
    logout() {
        // Remove the token stored in the browsers local storage.
        localStorage.removeItem(Constants.LOCAL_STORAGE.SESSION);
        // Set logged in user info to null.
        this.user.next(null);
        // Close the WebSocket.
        this.messageService.disconnect();
        // Redirect to the Login page.
        this.router.navigate(['/login']);
    }

    /**
     * Check if a token is stored in the browsers local storage, and update the
     * logged in user info.
     */
    autoLogin() {
        // Get the session from the local storage.
        const user: {
            fullname: string,
            token: string
        } = JSON.parse(localStorage.getItem(Constants.LOCAL_STORAGE.SESSION));

        // In case there are no data stored in the local storage, return.
        if (!user) {
            return;
        }
        // Init a Auth object and update the logged in user info.
        const loadedUser = new AuthDTO(user.fullname, user.token);
        if (loadedUser.token) {
            this.user.next(loadedUser);
            // Connect to the WebSocket.
            this.messageService.connect();
        }
    }

    /**
     * Send a new password request email.
     * 
     * @param forgotPassword ForgotPasswordDTO which contains the email and language.
     */
    forgotPassword(forgotPassword: ForgotPasswordDTO) {
        return this.http.post(Constants.API + '/' + this.endpoint + '/forgotPassword', forgotPassword);
    }

    /**
     * Validate the given reset password id.
     * 
     * @param id The encrypted id to be validated.
     */
    validateResetPasswordRequest(id: string) {
        return this.http.get(Constants.API + '/' + this.endpoint + '/validateResetPassword?id=' + encodeURIComponent(id));
    }

    /**
     * Change the user's password.
     * 
     * @param resetPassword Contains the info about the new password.
     */
    changePassword(resetPassword: ResetPasswordDTO) {
        return this.http.post(Constants.API + '/' + this.endpoint + '/changePassword', resetPassword);
    }
}