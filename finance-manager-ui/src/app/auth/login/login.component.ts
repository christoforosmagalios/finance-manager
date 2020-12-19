import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';

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

  constructor() { }

  ngOnInit(): void {
  }
  
  /**
   * Submit login form.
   * 
   * @param form The Form.
   */
  onSubmit(form: NgForm) {

    if (!form.valid) {
      return;
    }

  }
}
