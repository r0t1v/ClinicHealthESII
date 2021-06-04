import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITipoConvenio, getTipoConvenioIdentifier } from '../tipo-convenio.model';

export type EntityResponseType = HttpResponse<ITipoConvenio>;
export type EntityArrayResponseType = HttpResponse<ITipoConvenio[]>;

@Injectable({ providedIn: 'root' })
export class TipoConvenioService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/tipo-convenios');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(tipoConvenio: ITipoConvenio): Observable<EntityResponseType> {
    return this.http.post<ITipoConvenio>(this.resourceUrl, tipoConvenio, { observe: 'response' });
  }

  update(tipoConvenio: ITipoConvenio): Observable<EntityResponseType> {
    return this.http.put<ITipoConvenio>(`${this.resourceUrl}/${getTipoConvenioIdentifier(tipoConvenio) as number}`, tipoConvenio, {
      observe: 'response',
    });
  }

  partialUpdate(tipoConvenio: ITipoConvenio): Observable<EntityResponseType> {
    return this.http.patch<ITipoConvenio>(`${this.resourceUrl}/${getTipoConvenioIdentifier(tipoConvenio) as number}`, tipoConvenio, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITipoConvenio>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITipoConvenio[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addTipoConvenioToCollectionIfMissing(
    tipoConvenioCollection: ITipoConvenio[],
    ...tipoConveniosToCheck: (ITipoConvenio | null | undefined)[]
  ): ITipoConvenio[] {
    const tipoConvenios: ITipoConvenio[] = tipoConveniosToCheck.filter(isPresent);
    if (tipoConvenios.length > 0) {
      const tipoConvenioCollectionIdentifiers = tipoConvenioCollection.map(
        tipoConvenioItem => getTipoConvenioIdentifier(tipoConvenioItem)!
      );
      const tipoConveniosToAdd = tipoConvenios.filter(tipoConvenioItem => {
        const tipoConvenioIdentifier = getTipoConvenioIdentifier(tipoConvenioItem);
        if (tipoConvenioIdentifier == null || tipoConvenioCollectionIdentifiers.includes(tipoConvenioIdentifier)) {
          return false;
        }
        tipoConvenioCollectionIdentifiers.push(tipoConvenioIdentifier);
        return true;
      });
      return [...tipoConveniosToAdd, ...tipoConvenioCollection];
    }
    return tipoConvenioCollection;
  }
}
