import { HttpClient } from '@angular/common/http';
import { Injectable } from "@angular/core";
import { Router } from '@angular/router';
import { BehaviorSubject } from 'rxjs';
import { map } from 'rxjs/operators';
import { AuthDTO } from '../dto/auth-dto';
import { LoginDTO } from '../dto/login-dto';
import { RegisterDTO } from '../dto/register-dto';
import { Constants } from '../shared/constants/constants';

@Injectable({providedIn: 'root'})
export class AuthService {
    private endpoint = Constants.ENTITY.AUTH;
    user = new BehaviorSubject<AuthDTO>(null);

    constructor(
        private http: HttpClient,
        private router: Router) {}

    /**
     * Register the user with the given information.
     * 
     * @param registerDTO Object with the registration info.
     */
    register(registerDTO: RegisterDTO) {
        return this.http.post(Constants.API + '/' + this.endpoint + '/register', registerDTO);
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
        localStorage.setItem(Constants.LOCAL_STORAGE_KEY, JSON.stringify(user));
    }

    /**
     * Delete the token stored in the local storage and redirect to the Login page.
     */
    logout() {
        // Remove the token stored in the browsers local storage.
        localStorage.removeItem(Constants.LOCAL_STORAGE_KEY);
        // Set logged in user info to null.
        this.user.next(null);
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
        } = JSON.parse(localStorage.getItem(Constants.LOCAL_STORAGE_KEY));

        // In case there are no data stored in the local storage, return.
        if (!user) {
            this.router.navigate(['/login']);
        }
        // Init a Auth object and update the logged in user info.
        const loadedUser = new AuthDTO(user.fullname, user.token);
        if (loadedUser.token) {
            this.user.next(loadedUser);
        }
    }
}