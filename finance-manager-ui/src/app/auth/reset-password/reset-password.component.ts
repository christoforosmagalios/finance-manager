import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { ActivatedRoute, Params, Router } from '@angular/router';
import { ForgotPasswordDTO } from 'src/app/dto/forgot-password-dto';
import { ResetPasswordDTO } from 'src/app/dto/reset-password-dto';
import { LoaderService } from 'src/app/shared/components/loader/loader.service';
import { UtilsService } from 'src/app/shared/services/utils.service';
import { AuthService } from '../auth.service';

@Component({
  selector: 'app-reset-password',
  templateUrl: './reset-password.component.html',
  styleUrls: ['./reset-password.component.css']
})
export class ResetPasswordComponent implements OnInit {

  resetPassword = new ResetPasswordDTO();
  // Error object.
  errors = {
    password: null
  };

  constructor(
    private route: ActivatedRoute,
    private auth: AuthService,
    private router: Router,
    private loader: LoaderService,
    private utils: UtilsService) { }

  ngOnInit(): void {
    // Get the params.
    this.route.params.subscribe((params: Params) => {
      this.resetPassword.resetPasswordId = params['id'];
    });
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
    this.auth.changePassword(this.resetPassword).subscribe(res => {
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
