import { Component, OnInit } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { AuthService } from './auth/auth.service';
import { Constants } from './shared/constants/constants';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html'
})
export class AppComponent implements OnInit {

  title = 'Finance Manager';

  constructor(
    private translate: TranslateService,
    private auth: AuthService) { }

  ngOnInit(): void {
    let language = localStorage.getItem(Constants.LOCAL_STORAGE.LANGUAGE);
    if (language) {
      this.translate.use(language);
    } else {
      this.translate.use(Constants.DEFAULT_LANGUAGE);
    }
    this.auth.autoLogin();
  }



}
