import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IContaClinica, getContaClinicaIdentifier } from '../conta-clinica.model';

export type EntityResponseType = HttpResponse<IContaClinica>;
export type EntityArrayResponseType = HttpResponse<IContaClinica[]>;

@Injectable({ providedIn: 'root' })
export class ContaClinicaService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/conta-clinicas');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(contaClinica: IContaClinica): Observable<EntityResponseType> {
    return this.http.post<IContaClinica>(this.resourceUrl, contaClinica, { observe: 'response' });
  }

  update(contaClinica: IContaClinica): Observable<EntityResponseType> {
    return this.http.put<IContaClinica>(`${this.resourceUrl}/${getContaClinicaIdentifier(contaClinica) as number}`, contaClinica, {
      observe: 'response',
    });
  }

  partialUpdate(contaClinica: IContaClinica): Observable<EntityResponseType> {
    return this.http.patch<IContaClinica>(`${this.resourceUrl}/${getContaClinicaIdentifier(contaClinica) as number}`, contaClinica, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IContaClinica>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IContaClinica[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addContaClinicaToCollectionIfMissing(
    contaClinicaCollection: IContaClinica[],
    ...contaClinicasToCheck: (IContaClinica | null | undefined)[]
  ): IContaClinica[] {
    const contaClinicas: IContaClinica[] = contaClinicasToCheck.filter(isPresent);
    if (contaClinicas.length > 0) {
      const contaClinicaCollectionIdentifiers = contaClinicaCollection.map(
        contaClinicaItem => getContaClinicaIdentifier(contaClinicaItem)!
      );
      const contaClinicasToAdd = contaClinicas.filter(contaClinicaItem => {
        const contaClinicaIdentifier = getContaClinicaIdentifier(contaClinicaItem);
        if (contaClinicaIdentifier == null || contaClinicaCollectionIdentifiers.includes(contaClinicaIdentifier)) {
          return false;
        }
        contaClinicaCollectionIdentifiers.push(contaClinicaIdentifier);
        return true;
      });
      return [...contaClinicasToAdd, ...contaClinicaCollection];
    }
    return contaClinicaCollection;
  }
}
