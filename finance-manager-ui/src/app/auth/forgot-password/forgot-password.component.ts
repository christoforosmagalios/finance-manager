import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { ForgotPasswordDTO } from 'src/app/dto/forgot-password-dto';
import { LoaderService } from 'src/app/shared/components/loader/loader.service';
import { UtilsService } from 'src/app/shared/services/utils.service';
import { AuthService } from '../auth.service';

@Component({
  selector: 'app-forgot-password',
  templateUrl: './forgot-password.component.html',
  styleUrls: ['./forgot-password.component.css']
})
export class ForgotPasswordComponent implements OnInit {

  // Email to send the new password request.
  email: string;
   // Error object.
   errors = {
    email: null
  };

  constructor(
    private auth: AuthService,
    private router: Router,
    private loader: LoaderService,
    private utils: UtilsService,
    private translate: TranslateService) { }

  ngOnInit(): void {
  }

  /**
   * Submit login form.
   * 
   * @param form The Form.
   */
   onSubmit(form: NgForm) {

    // In case the form is invalid, do not proceed.
    if (!form.valid) {
      return;
    }
    // Clear form errors.
    this.utils.clearErrors(this.errors);
    // Show the loading spinner.
    this.loader.show();
    this.auth.forgotPassword(new ForgotPasswordDTO(this.email, this.translate.currentLang)).subscribe(res => {
      // Hide the loading spinner.
      this.loader.hide();
      // Navigate to main view.
      this.router.navigate(["/"]);
    }, error => {
      // Mark the validation errors.
      this.utils.markErrors(this.errors, error);
    });

  }
}
