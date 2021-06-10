import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Router } from '@angular/router';
import { UserDetailsDTO } from 'src/app/dto/user-details-dto';
import { LoaderService } from 'src/app/shared/components/loader/loader.service';
import { UtilsService } from 'src/app/shared/services/utils.service';
import { AuthService } from '../auth.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {

  // Register object.
  user = new UserDetailsDTO();
  // Error object.
  errors = {
    username: null,
    email: null,
    password: null,
    firstName: null,
    lastName: null
  };

  constructor(
    private auth: AuthService,
    private loader: LoaderService,
    private router: Router,
    private utils: UtilsService) { }

  ngOnInit(): void {
    // Do nothing.
  }

  /**
   * Submit the registration form.
   * 
   * @param form The Form.
   */
  onSubmit(form: NgForm) {

    if (!form.valid) {
      return;
    }
    // Clear form errors.
    this.utils.clearErrors(this.errors);
    // Show the Loader.
    this.loader.show();
    // Register user.
    this.auth.register(this.user).subscribe(res => {
      // Hide the Loader.
      this.loader.hide();
      // Navigate to the Login page.
      this.router.navigate(["/login"]);
    }, error => {
      // Mark the validation errors.
      this.utils.markErrors(this.errors, error);
    });
  }
}
