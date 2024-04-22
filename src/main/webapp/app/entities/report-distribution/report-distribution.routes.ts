import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ReportDistributionComponent } from './list/report-distribution.component';
import { ReportDistributionDetailComponent } from './detail/report-distribution-detail.component';
import { ReportDistributionUpdateComponent } from './update/report-distribution-update.component';
import ReportDistributionResolve from './route/report-distribution-routing-resolve.service';

const reportDistributionRoute: Routes = [
  {
    path: '',
    component: ReportDistributionComponent,
    data: {},
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':rid/view',
    component: ReportDistributionDetailComponent,
    resolve: {
      reportDistribution: ReportDistributionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ReportDistributionUpdateComponent,
    resolve: {
      reportDistribution: ReportDistributionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':rid/edit',
    component: ReportDistributionUpdateComponent,
    resolve: {
      reportDistribution: ReportDistributionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default reportDistributionRoute;
