import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { DatePipe } from '@angular/common';

import { Feedback } from './feedback';

@Injectable({
  providedIn: 'root'
})
export class FeedbackService {
  private apiUrl = 'http://localhost:8080/api/v1/feedback';

  constructor(private http: HttpClient,
    private datePipe: DatePipe) { }

  findPageByDateBetween(start: Date,
    end: Date,
    page: number,
    size: number): Observable<Feedback[]> {
    const url = `${this.apiUrl}/between`;
    const startDate: any = this.datePipe.transform(start, 'yyyy-MM-dd');
    const endDate: any = this.datePipe.transform(end, 'yyyy-MM-dd');

    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('start', startDate)
      .set('end', endDate);

    const headers = new HttpHeaders().set('Content-Type', 'application/json');

    return this.http.get<Feedback[]>(url, { headers, params });
  }
}
