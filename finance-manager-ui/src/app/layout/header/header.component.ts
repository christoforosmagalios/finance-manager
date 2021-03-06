import { Component, OnInit } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { Subscription } from 'rxjs';
import { AuthService } from 'src/app/auth/auth.service';
import { Constants } from 'src/app/shared/constants/constants';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {
  // Navigation is collapsed flag.
  isCollapsed = true;
  // Application Languages.
  languages = Constants.LANGUAGES;
  // Application default language.
  activeLanguage = Constants.DEFAULT_LANGUAGE;
  // Subscription to logged in user info.
  private subscription: Subscription;
  // Logged in user.
  isLoggedInUser = null;

  constructor(
    private translate: TranslateService,
    private auth: AuthService) { }

  ngOnInit(): void {
    this.activeLanguage = this.translate.currentLang;
    this.subscription = this.auth.user.subscribe(user => {
      this.isLoggedInUser = user;
    });
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }

  /**
   * Update the UI language and store it in the local storage.
   * 
   * @param language The selected language. 
   */
  setLanguage(language: string) {
    this.translate.use(language);
    this.activeLanguage = language;
    // Store the selected ui language in the local storage.
    localStorage.setItem(Constants.LOCAL_STORAGE.LANGUAGE, language);
  }

  /**
   * Logout.
   */
  logout() {
    this.auth.logout();
  }
}
