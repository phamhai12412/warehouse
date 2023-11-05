import { Button, TableCell, TableRow, TextField } from '@mui/material';

import CloseIcon from '@mui/icons-material/Close';
import React from 'react'
import { Toaster } from 'react-hot-toast';

function FormShipper({ result, handleCloseForm, handleChangeForm, handleAddShipper, errorShipName, errorPrice }) {

  const styled1 = { padding: '4px 10px', borderRadius: '5px', border: '2px solid #000', outline: 'none', width: '50%', textAlign: 'center' };

  return (
    <TableRow sx={{ '&:last-child td, &:last-child th': { border: 0 } }}>
      <TableCell align='center'><CloseIcon onClick={handleCloseForm} /></TableCell>
      <TableCell align='center'>
        <TextField
          error={errorShipName}
          id="outlined-basic"
          autoFocus fullWidth
          size='small'
          label={errorShipName ? errorShipName : "Tên dơn vị vận chuyển"}
          defaultValue={result.shipName}
          onChange={handleChangeForm}
          name="shipName"
          variant="outlined"
          required
        />
      </TableCell>
      <TableCell align='center'>
        <TextField
          type='number'
          error={errorPrice}
          id="outlined-basic"
          fullWidth
          size='small'
          label={errorPrice ? errorPrice : "Giá vận chuyển / kg"}
          defaultValue={result.price}
          onChange={handleChangeForm}
          name="price"
          variant="outlined"
          required
        />
      </TableCell>
      <TableCell align='center'><i className="fa-solid fa-lock-open"></i></TableCell>
      <TableCell align='center'><Button variant="contained" onClick={handleAddShipper}>THÊM</Button></TableCell>
      <Toaster />
    </TableRow>
  )
}

export default FormShipper