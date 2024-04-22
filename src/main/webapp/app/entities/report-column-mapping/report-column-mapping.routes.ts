import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ReportColumnMappingComponent } from './list/report-column-mapping.component';
import { ReportColumnMappingDetailComponent } from './detail/report-column-mapping-detail.component';
import { ReportColumnMappingUpdateComponent } from './update/report-column-mapping-update.component';
import ReportColumnMappingResolve from './route/report-column-mapping-routing-resolve.service';

const reportColumnMappingRoute: Routes = [
  {
    path: '',
    component: ReportColumnMappingComponent,
    data: {},
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':rid/view',
    component: ReportColumnMappingDetailComponent,
    resolve: {
      reportColumnMapping: ReportColumnMappingResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ReportColumnMappingUpdateComponent,
    resolve: {
      reportColumnMapping: ReportColumnMappingResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':rid/edit',
    component: ReportColumnMappingUpdateComponent,
    resolve: {
      reportColumnMapping: ReportColumnMappingResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default reportColumnMappingRoute;
