import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IReportParam, NewReportParam } from '../report-param.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { rid: unknown }> = Partial<Omit<T, 'rid'>> & { rid: T['rid'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IReportParam for edit and NewReportParamFormGroupInput for create.
 */
type ReportParamFormGroupInput = IReportParam | PartialWithRequiredKeyOf<NewReportParam>;

type ReportParamFormDefaults = Pick<NewReportParam, 'rid'>;

type ReportParamFormGroupContent = {
  rid: FormControl<IReportParam['rid'] | NewReportParam['rid']>;
  name: FormControl<IReportParam['name']>;
  type: FormControl<IReportParam['type']>;
  value: FormControl<IReportParam['value']>;
  conversionRule: FormControl<IReportParam['conversionRule']>;
  report: FormControl<IReportParam['report']>;
};

export type ReportParamFormGroup = FormGroup<ReportParamFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ReportParamFormService {
  createReportParamFormGroup(reportParam: ReportParamFormGroupInput = { rid: null }): ReportParamFormGroup {
    const reportParamRawValue = {
      ...this.getFormDefaults(),
      ...reportParam,
    };
    return new FormGroup<ReportParamFormGroupContent>({
      rid: new FormControl(
        { value: reportParamRawValue.rid, disabled: reportParamRawValue.rid !== null },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(reportParamRawValue.name),
      type: new FormControl(reportParamRawValue.type),
      value: new FormControl(reportParamRawValue.value),
      conversionRule: new FormControl(reportParamRawValue.conversionRule),
      report: new FormControl(reportParamRawValue.report),
    });
  }

  getReportParam(form: ReportParamFormGroup): IReportParam | NewReportParam {
    return form.getRawValue() as IReportParam | NewReportParam;
  }

  resetForm(form: ReportParamFormGroup, reportParam: ReportParamFormGroupInput): void {
    const reportParamRawValue = { ...this.getFormDefaults(), ...reportParam };
    form.reset(
      {
        ...reportParamRawValue,
        rid: { value: reportParamRawValue.rid, disabled: reportParamRawValue.rid !== null },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ReportParamFormDefaults {
    return {
      rid: null,
    };
  }
}
