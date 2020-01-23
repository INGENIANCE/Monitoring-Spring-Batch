import { Component, OnInit } from '@angular/core';
import { SchedulerService } from './services/scheduler.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.less']
})
export class AppComponent implements OnInit {
  constructor(private schedulerService: SchedulerService) {}

  private configBackup: string;

  public schedulerState: string;
  public cronExpression: string;
  public onError = false;
  public configMessage: string = null;

  ngOnInit() {
    // Get the current status.
    this.getScheduler();
    // Get current Cron expression.
    this.retrieveConfig();
  }

  /**
   * Get the current scheduler status.
   */
  public getScheduler = () => {
    this.schedulerService.getStatus().subscribe(
      (response: any) => {
        this.schedulerState = response.data;
      },
      error => {
        console.error(error);
      }
    );
  }

  /**
   * Start scheduler.
   */
  public startScheduler = () => {
    this.schedulerService.startScheduler().subscribe(
      () => {
        this.schedulerState = 'running';
      },
      error => {
        console.error(JSON.stringify(error));
      }
    );
  }

  /**
   * Stop scheduler.
   */
  public stopScheduler = () => {
    this.schedulerService.stopScheduler().subscribe(
      () => {
        this.schedulerState = 'stopped';
      },
      error => {
        console.error(JSON.stringify(error));
      }
    );
  }

  /**
   * Pause scheduler.
   */
  public pauseScheduler = () => {
    this.schedulerService.pauseScheduler().subscribe(
      () => {
        this.schedulerState = 'paused';
      },
      error => {
        console.error(JSON.stringify(error));
      }
    );
  }

  /**
   * Resume scheduler.
   */
  public resumeScheduler = () => {
    this.schedulerService.resumeScheduler().subscribe(
      () => {
        this.schedulerState = 'running';
      },
      error => {
        console.error(JSON.stringify(error));
      }
    );
  }

  /**
   * Start or pause scheduler.
   */
  public startOrPause = () => {
    switch (this.schedulerState) {
      case 'running':
        this.pauseScheduler();
        break;
      case 'paused':
        this.resumeScheduler();
        break;
      default:
        this.startScheduler();
        break;
    }
  }

  /**
   * Get current Cron Expression from Batch-SX API REST.
   */
  public retrieveConfig = () => {
    this.schedulerService.getConfig()
      .subscribe((response: any) => {
        this.cronExpression = response.data;
        this.configBackup = response.data;
      });
  }

  /**
   * Define a new Cron expression to manage batch-sx execution process.
   */
  public submitConfig = () => {
    this.schedulerService.updateConfig(this.cronExpression)
      .subscribe(() => {
        this.onError = false;
        this.configBackup = this.cronExpression;
        this.configMessage = 'Update of the new schedule was successfully completed.';
      }, error => {
        this.onError = true;
        this.configMessage = error;
        this.cronExpression = this.configBackup;
      });
  }
}
