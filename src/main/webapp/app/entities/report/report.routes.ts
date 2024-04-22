import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ReportComponent } from './list/report.component';
import { ReportDetailComponent } from './detail/report-detail.component';
import { ReportUpdateComponent } from './update/report-update.component';
import ReportResolve from './route/report-routing-resolve.service';

const reportRoute: Routes = [
  {
    path: '',
    component: ReportComponent,
    data: {},
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':rid/view',
    component: ReportDetailComponent,
    resolve: {
      report: ReportResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ReportUpdateComponent,
    resolve: {
      report: ReportResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':rid/edit',
    component: ReportUpdateComponent,
    resolve: {
      report: ReportResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default reportRoute;
