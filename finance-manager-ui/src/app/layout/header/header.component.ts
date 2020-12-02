import { Component, OnInit } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { Constants } from 'src/app/shared/constants/constants';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

  isCollapsed = true;
  languages = Constants.LANGUAGES;
  activeLanguage = Constants.DEFAULT_LANGUAGE;

  constructor(private translate: TranslateService) { }

  ngOnInit(): void {
    this.activeLanguage = this.translate.currentLang;
  }

  setLanguage(language: string) {
    this.translate.use(language);
    this.activeLanguage = language;
  }
}
