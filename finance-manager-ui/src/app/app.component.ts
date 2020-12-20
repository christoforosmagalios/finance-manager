import { Component, OnInit } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { AuthService } from './auth/auth.service';
import { Constants } from './shared/constants/constants';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {

  title = 'Finance Manager';

  constructor(
    private translate: TranslateService,
    private auth: AuthService) { }

  ngOnInit(): void {
    this.translate.use(Constants.DEFAULT_LANGUAGE);
    this.auth.autoLogin();
  }



}
