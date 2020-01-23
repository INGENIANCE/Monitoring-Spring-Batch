import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable()
export class SchedulerService {
  constructor(private http: HttpClient) {}

  startScheduler = () => {
    return this.http.get('/batch-spring/run');
  }

  stopScheduler = () => {
    return this.http.get('/batch-spring/stop');
  }

  pauseScheduler = () => {
    return this.http.get('/batch-spring/pause');
  }

  resumeScheduler = () => {
    return this.http.get('/batch-spring/resume');
  }

  getStatus = () => {
    return this.http.get('/batch-spring');
  }

  getConfig = () => {
    return this.http.get('/batch-spring/config');
  }

  updateConfig = (cronExpression: string) => {
    return this.http.post('/batch-spring/config', {cronExpression});
  }
}
