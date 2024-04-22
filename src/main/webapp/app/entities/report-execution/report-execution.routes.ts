import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ReportExecutionComponent } from './list/report-execution.component';
import { ReportExecutionDetailComponent } from './detail/report-execution-detail.component';
import { ReportExecutionUpdateComponent } from './update/report-execution-update.component';
import ReportExecutionResolve from './route/report-execution-routing-resolve.service';

const reportExecutionRoute: Routes = [
  {
    path: '',
    component: ReportExecutionComponent,
    data: {},
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':rid/view',
    component: ReportExecutionDetailComponent,
    resolve: {
      reportExecution: ReportExecutionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ReportExecutionUpdateComponent,
    resolve: {
      reportExecution: ReportExecutionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':rid/edit',
    component: ReportExecutionUpdateComponent,
    resolve: {
      reportExecution: ReportExecutionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default reportExecutionRoute;
