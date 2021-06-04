import { IPaciente } from 'app/entities/paciente/paciente.model';

export interface IEnderecoPaciente {
  id?: number;
  cidade?: string | null;
  logradouro?: string | null;
  bairro?: string | null;
  numero?: number | null;
  referencia?: string | null;
  cep?: string | null;
  paciente?: IPaciente | null;
}

export class EnderecoPaciente implements IEnderecoPaciente {
  constructor(
    public id?: number,
    public cidade?: string | null,
    public logradouro?: string | null,
    public bairro?: string | null,
    public numero?: number | null,
    public referencia?: string | null,
    public cep?: string | null,
    public paciente?: IPaciente | null
  ) {}
}

export function getEnderecoPacienteIdentifier(enderecoPaciente: IEnderecoPaciente): number | undefined {
  return enderecoPaciente.id;
}
