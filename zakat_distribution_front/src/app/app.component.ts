import { Component } from '@angular/core';
import {TranslateService} from "@ngx-translate/core";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'zakat_distribution_front';

  constructor(private translate: TranslateService) {
    translate.setDefaultLang('fr');
    translate.use('fr');
  }
}
