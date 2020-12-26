import { HttpClient } from '@angular/common/http';
import { Injectable } from "@angular/core";
import { UserDetailsDTO } from '../dto/user-details-dto';
import { Constants } from '../shared/constants/constants';

@Injectable({providedIn: 'root'})
export class UserService {
    endpoint = Constants.ENTITY.USER;

    constructor(private http: HttpClient) {}

    /**
     * Find the logged in user.
     */
    findLoggedInUser() {
        return this.http.get(Constants.API + '/' +this.endpoint + '/current');
    }

    /**
     * Save the given user details.
     * 
     * @param user The user details.
     */
    update(user: UserDetailsDTO) {
        return this.http.post(Constants.API + '/' +this.endpoint + '/update', user);
    }
}