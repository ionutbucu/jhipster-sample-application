import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ReportMetadataComponent } from './list/report-metadata.component';
import { ReportMetadataDetailComponent } from './detail/report-metadata-detail.component';
import { ReportMetadataUpdateComponent } from './update/report-metadata-update.component';
import ReportMetadataResolve from './route/report-metadata-routing-resolve.service';

const reportMetadataRoute: Routes = [
  {
    path: '',
    component: ReportMetadataComponent,
    data: {},
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':rid/view',
    component: ReportMetadataDetailComponent,
    resolve: {
      reportMetadata: ReportMetadataResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ReportMetadataUpdateComponent,
    resolve: {
      reportMetadata: ReportMetadataResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':rid/edit',
    component: ReportMetadataUpdateComponent,
    resolve: {
      reportMetadata: ReportMetadataResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default reportMetadataRoute;
