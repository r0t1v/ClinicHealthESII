import { IContaClinica } from 'app/entities/conta-clinica/conta-clinica.model';

export interface ITipoConvenio {
  id?: number;
  convenio?: string | null;
  codigoconvenio?: number | null;
  nomeconvenio?: string | null;
  contato?: string | null;
  contaClinica?: IContaClinica | null;
}

export class TipoConvenio implements ITipoConvenio {
  constructor(
    public id?: number,
    public convenio?: string | null,
    public codigoconvenio?: number | null,
    public nomeconvenio?: string | null,
    public contato?: string | null,
    public contaClinica?: IContaClinica | null
  ) {}
}

export function getTipoConvenioIdentifier(tipoConvenio: ITipoConvenio): number | undefined {
  return tipoConvenio.id;
}
