import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Router } from '@angular/router';
import { RegisterDTO } from 'src/app/dto/register-dto';
import { LoaderService } from 'src/app/shared/components/loader/loader.service';
import { AuthService } from '../auth.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {

  // Register object.
  register = new RegisterDTO();
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
    private router: Router) { }

  ngOnInit(): void {
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
    this.clearErrors();
    this.loader.show();
    this.auth.register(this.register).subscribe(res => {
      this.loader.hide();
      this.router.navigate(["/login"]);
    }, error => {
      if (error && error.errors instanceof Array) {
        for (let e of error.errors) {
          this.errors[e.field] = e.defaultMessage;
        }
      }
    });
    
  }
  
  /**
   * Clear any previous validation errors.
   */
  private clearErrors() {
    for (let property in this.errors) {
      this.errors[property] = null;
    }
  }
}
