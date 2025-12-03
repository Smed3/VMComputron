

function Chevron({direction='down'}) {
  if (direction === 'down') {
    return (
      <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 16 16" fill="none">
        <path d="M4 6L8 10L12 6" stroke-width="1.6" stroke-linecap="round" stroke-linejoin="round"/>
      </svg>
    )
  } else if (direction === 'up') {
    return (
      <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 16 16" fill="none">
        <path d="M12 10L8 6L4 10" stroke-width="1.6" stroke-linecap="round" stroke-linejoin="round"/>
      </svg>
    )
  }
}

export default Chevron;