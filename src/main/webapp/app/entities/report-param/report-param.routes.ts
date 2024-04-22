import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ReportParamComponent } from './list/report-param.component';
import { ReportParamDetailComponent } from './detail/report-param-detail.component';
import { ReportParamUpdateComponent } from './update/report-param-update.component';
import ReportParamResolve from './route/report-param-routing-resolve.service';

const reportParamRoute: Routes = [
  {
    path: '',
    component: ReportParamComponent,
    data: {},
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':rid/view',
    component: ReportParamDetailComponent,
    resolve: {
      reportParam: ReportParamResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ReportParamUpdateComponent,
    resolve: {
      reportParam: ReportParamResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':rid/edit',
    component: ReportParamUpdateComponent,
    resolve: {
      reportParam: ReportParamResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default reportParamRoute;
