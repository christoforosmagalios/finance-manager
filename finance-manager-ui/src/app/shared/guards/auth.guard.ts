import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, Router } from "@angular/router";
import { Injectable } from "@angular/core";
import { AuthService } from "src/app/auth/auth.service";

@Injectable({ providedIn: 'root' })
export class AuthGuard implements CanActivate {

    constructor(private auth: AuthService, private router: Router) { }

    canActivate(route: ActivatedRouteSnapshot, router: RouterStateSnapshot) {
        // Get logged in user info.
        let user = this.auth.user;
        // If the logged in user info are available return true.
        if (user && user.getValue()) {
            return true;
        }
        // User is not logged in. Redirect to the login page.
        this.router.navigate(['/login']);
        return false;
    }
}