import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IResultadoExame, getResultadoExameIdentifier } from '../resultado-exame.model';

export type EntityResponseType = HttpResponse<IResultadoExame>;
export type EntityArrayResponseType = HttpResponse<IResultadoExame[]>;

@Injectable({ providedIn: 'root' })
export class ResultadoExameService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/resultado-exames');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(resultadoExame: IResultadoExame): Observable<EntityResponseType> {
    return this.http.post<IResultadoExame>(this.resourceUrl, resultadoExame, { observe: 'response' });
  }

  update(resultadoExame: IResultadoExame): Observable<EntityResponseType> {
    return this.http.put<IResultadoExame>(`${this.resourceUrl}/${getResultadoExameIdentifier(resultadoExame) as number}`, resultadoExame, {
      observe: 'response',
    });
  }

  partialUpdate(resultadoExame: IResultadoExame): Observable<EntityResponseType> {
    return this.http.patch<IResultadoExame>(
      `${this.resourceUrl}/${getResultadoExameIdentifier(resultadoExame) as number}`,
      resultadoExame,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IResultadoExame>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IResultadoExame[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addResultadoExameToCollectionIfMissing(
    resultadoExameCollection: IResultadoExame[],
    ...resultadoExamesToCheck: (IResultadoExame | null | undefined)[]
  ): IResultadoExame[] {
    const resultadoExames: IResultadoExame[] = resultadoExamesToCheck.filter(isPresent);
    if (resultadoExames.length > 0) {
      const resultadoExameCollectionIdentifiers = resultadoExameCollection.map(
        resultadoExameItem => getResultadoExameIdentifier(resultadoExameItem)!
      );
      const resultadoExamesToAdd = resultadoExames.filter(resultadoExameItem => {
        const resultadoExameIdentifier = getResultadoExameIdentifier(resultadoExameItem);
        if (resultadoExameIdentifier == null || resultadoExameCollectionIdentifiers.includes(resultadoExameIdentifier)) {
          return false;
        }
        resultadoExameCollectionIdentifiers.push(resultadoExameIdentifier);
        return true;
      });
      return [...resultadoExamesToAdd, ...resultadoExameCollection];
    }
    return resultadoExameCollection;
  }
}
