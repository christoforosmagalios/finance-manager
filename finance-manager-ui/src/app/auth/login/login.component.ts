import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Router } from '@angular/router';
import { LoginDTO } from 'src/app/dto/login-dto';
import { LoaderService } from 'src/app/shared/components/loader/loader.service';
import { AuthService } from '../auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  // Username or email.
  username: string;
  // Password.
  password: string;
  // Invalid credentials.
  invlaidCredentials = false;

  constructor(
    private auth: AuthService,
    private router: Router,
    private loader: LoaderService) { }

  ngOnInit(): void {
    // Do nothing.
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
    // Clear the error message.
    this.invlaidCredentials = false;
    // Show the loading spinner.
    this.loader.show();
    this.auth.login(new LoginDTO(this.username, this.password)).subscribe(res => {
      // Hide the loading spinner.
      this.loader.hide();
      // Navigate to main view.
      this.router.navigate(["/"]);
    }, error => {
      // Display the error message.
      this.invlaidCredentials = true;
    });

  }
}
