import { Component, OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';

import { FeedbackService } from './feedback/feedback.service';
import { Feedback } from './feedback/feedback';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  currentDate: any;
  feedbacks: Feedback[] = [];

  constructor(private datePipe: DatePipe,
    private feedbackService: FeedbackService) {

  }

  ngOnInit(): void {
    this.currentDate = this.datePipe.transform(new Date(), 'EEEE, MMMM d, y');
    this.feedbackService.findPageByDateBetween(new Date(), new Date(), 1, 10)
    .subscribe(data => {
      this.feedbacks = data;
    }, error => {
      console.log(error);
    })
  }


}
