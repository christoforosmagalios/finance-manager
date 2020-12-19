import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { RegisterDTO } from 'src/app/dto/register-dto';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {

  // Register object.
  register = new RegisterDTO();

  constructor() { }

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
    
  }
}
