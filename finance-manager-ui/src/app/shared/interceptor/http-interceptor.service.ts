import { HttpErrorResponse, HttpHandler, HttpInterceptor, HttpRequest } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { UtilsService } from "../services/utils.service";
import { catchError } from 'rxjs/operators';
import { throwError } from "rxjs";
import { Messages } from "../constants/messages";
import { LoaderService } from "../components/loader/loader.service";

@Injectable({providedIn: 'root'})
export class HttpInterceptorService implements HttpInterceptor {
  constructor(
    private utilsService: UtilsService,
    private loader: LoaderService) {}

  intercept(request: HttpRequest<any>, next: HttpHandler) {
    return next.handle(request)
        .pipe(
            catchError((error: HttpErrorResponse) => {
                // Hide loader.
                this.loader.hide();
                // Display error toaster.
                this.utilsService.showError(Messages.GENERIC_ERROR);
                return throwError(error.error.message);
            })
        );
  }
}