import { HttpHandler, HttpHeaders, HttpInterceptor, HttpRequest } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { UtilsService } from "../services/utils.service";
import { catchError, exhaustMap, take } from 'rxjs/operators';
import { throwError } from "rxjs";
import { LoaderService } from "../components/loader/loader.service";
import { AuthService } from "src/app/auth/auth.service";
import { Constants } from "../constants/constants";

@Injectable({providedIn: 'root'})
export class HttpInterceptorService implements HttpInterceptor {

  constructor(
    private utilsService: UtilsService,
    private loader: LoaderService,
    private auth: AuthService) {}

  intercept(req: HttpRequest<any>, next: HttpHandler) {
    return this.auth.user.pipe(
          take(1),
          exhaustMap(user => {
            // Add the token to the request header if the user is logged in.
            if (user) {
              req = req.clone({
                headers: new HttpHeaders().set(Constants.HEADERS.AUTH, user.token)
              });
            }

            return next.handle(req).pipe(catchError((error: any) => {
              // Hide loader.
              this.loader.hide();
              if(error && error.status === 403) {
                this.auth.logout();
              } else if (error.error && error.error.message) {
                // Display error toaster.
                this.utilsService.showError(error.error.message);
                return throwError(error.error);
              }
              return throwError(error.message);
            }));
          })
        );
  }
}