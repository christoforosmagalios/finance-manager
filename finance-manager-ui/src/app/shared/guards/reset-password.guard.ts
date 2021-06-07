import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, Router } from "@angular/router";
import { Injectable } from "@angular/core";
import { AuthService } from "src/app/auth/auth.service";
import { Observable, of } from 'rxjs';
import { catchError, map } from "rxjs/operators";

@Injectable({ providedIn: 'root' })
export class ResetPasswordGuard implements CanActivate {

    constructor(
        private auth: AuthService, 
        private router: Router) { }

    canActivate(route: ActivatedRouteSnapshot, router: RouterStateSnapshot) {

        let id = route.params.id;
        if (!id) {
            return false;
        }

        return this.auth.validateResetPasswordRequest(id).pipe(
            map(e => {
              if (e) {
                return true;
              } else {
                this.router.navigate(['/login']);
                return false;
              }
            }),
            catchError((err) => {
              this.router.navigate(['/login']);
              return of(false);
            })
          );
    }
}